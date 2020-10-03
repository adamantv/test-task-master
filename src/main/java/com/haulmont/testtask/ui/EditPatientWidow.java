package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.*;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import java.util.List;

//класс для создания окна "Редактирование пациента"
public class EditPatientWidow {
    //добавление нового пациента
    public Window addPatient(Grid grid) {
        final Window window = new Window("Добавить нового пациента");
        window.setModal(true);
        window.center();
        window.setWidth("400");
        window.setHeight("300");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addPatient = new FormLayout();
        addPatient.setMargin(true);
        addPatient.setSizeFull();

        final TextField surname = new TextField("Фамилия", "");
        surname.setSizeFull();
        surname.setRequired(true);
        surname.setMaxLength(50);
        surname.setNullSettingAllowed(true);
        surname.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final TextField firstName = new TextField("Имя", "");
        firstName.setSizeFull();
        firstName.setRequired(true);
        firstName.setMaxLength(50);
        firstName.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final TextField patronymic = new TextField("Отчество", "");
        patronymic.setSizeFull();
        patronymic.setMaxLength(50);
        patronymic.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{0,50}", true, "Введите корректные данные"));

        final TextField number = new TextField("Номер телефона", "");
        number.setSizeFull();
        number.setRequired(true);
        number.setMaxLength(50);
        number.addValidator(new RegexpValidator("^((\\+?7|8)[ \\-] ?)?((\\(\\d{3}\\))|(\\d{3}))?([ \\-])?(\\d{3}[\\- ]?\\d{2}[\\- ]?\\d{2})$", true, "Веедите номер телефона в формате: +7 (ххх) ххх-хх-хх, 8 (ххх) ххх-хх-хх или ххх-хх-хх"));

        final Button savePatient = new Button("Добавить");
        savePatient.setStyleName(Reindeer.BUTTON_SMALL);

        savePatient.addClickListener(clickEvent -> {

            try {
                surname.validate();
                firstName.validate();
                number.validate();

                Database.startDatabase();
                Patient patient = new Patient(surname.getValue().trim(), firstName.getValue().trim(), patronymic.getValue().trim(), number.getValue().trim() );
                PatientDAO.addPatient(patient);
                List<Patient> patients = PatientDAO.getAllPatient();

                int index = patients.size();
                grid.addRow(patients.get(index-1).getId(), patients.get(index-1).getSurname(), patients.get(index-1).getFirstName(),
                        patients.get(index-1).getPatronymic(), patients.get(index-1).getNumber());

                Database.closeDatabase();
                window.close();
            } catch (Validator.InvalidValueException e) {

                surname.setRequiredError("Введите фамилию");
                firstName.setRequiredError("Введите имя");
                number.setRequiredError("Введите номер телефона в формате: +7 (ххх) ххх-хх-хх, 8 (ххх) ххх-хх-хх или ххх-хх-хх");
                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
        });

        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);
        cancel.addClickListener(clickEvent -> window.close());

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(savePatient);
        buttons.addComponent(cancel);

        addPatient.addComponent(surname);
        addPatient.addComponent(firstName);
        addPatient.addComponent(patronymic);
        addPatient.addComponent(number);
        addPatient.addComponent(buttons);

        window.setContent(addPatient);
        return window;
    }

    //удаление пациента
    public void deletePatient(Grid grid){
        if (grid.getSelectedRow() == null) return;

        Database.startDatabase();

        Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
        Database.startDatabase();
        int result = PatientDAO.deletePatient(id);
        Database.closeDatabase();

        if (result == 0) {
            grid.getContainerDataSource().removeItem(grid.getSelectedRow());
            Notification.show("Пациент удален из БД");
        } else if (result == 1) {

            Notification.show("Для данного пациента существует рецепт!", Notification.TYPE_ERROR_MESSAGE);
        } else Notification.show("Ошибка базы данных!",Notification.TYPE_ERROR_MESSAGE);
    }

    //внесение изменений в данные пациента
    public Window updatePatient(Grid grid) {
        final Window window = new Window("Изменить данные пациента");
        window.setModal(true);
        window.center();
        window.setWidth("400");
        window.setHeight("300");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addPatient = new FormLayout();
        addPatient.setMargin(true);
        addPatient.setSizeFull();

        final TextField surname = new TextField("Фамилия", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Фамилия").getValue().toString());
        surname.setSizeFull();
        surname.setMaxLength(50);
        surname.setRequired(true);
        surname.setRequiredError("Введите фамилию");
        surname.setNullSettingAllowed(true);
        surname.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final TextField firstName = new TextField("Имя", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Имя").getValue().toString());
        firstName.setSizeFull();
        firstName.setRequired(true);
        firstName.setRequiredError("Введите имя");
        firstName.setMaxLength(50);
        firstName.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final TextField patronymic = new TextField("Отчество", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Отчество").getValue().toString());
        patronymic.setSizeFull();
        patronymic.setMaxLength(50);
        patronymic.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{0,50}", true, "Введите корректные данные"));

        final TextField number = new TextField("Номер телефона", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Номер телефона").getValue().toString());
        number.setSizeFull();
        number.setRequired(true);
        number.setRequiredError("Введите номер телефона в формате: +7 (ххх) ххх-хх-хх, 8 (ххх) ххх-хх-хх или ххх-хх-хх");
        number.setMaxLength(50);
        number.addValidator(new RegexpValidator("^((\\+?7|8)[ \\-] ?)?((\\(\\d{3}\\))|(\\d{3}))?([ \\-])?(\\d{3}[\\- ]?\\d{2}[\\- ]?\\d{2})$", true, "Веедите номер телефона в формате: +7 (ххх) ххх-хх-хх, 8 (ххх) ххх-хх-хх или ххх-хх-хх"));

        final Button savePatient = new Button("Применить");
        savePatient.setStyleName(Reindeer.BUTTON_SMALL);


        savePatient.addClickListener(clickEvent -> {

            try {
                surname.validate();
                firstName.validate();
                number.validate();

                Database.startDatabase();
                Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
                Patient patient = new Patient(surname.getValue().trim(), firstName.getValue().trim(), patronymic.getValue().trim(), number.getValue().trim());
                patient.setId(id);
                PatientDAO.updatePatient(patient);
                Database.closeDatabase();

                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Фамилия").setValue(surname.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Имя").setValue(firstName.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Отчество").setValue(patronymic.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Номер телефона").setValue(number.getValue().trim());
                window.close();
            } catch (Validator.InvalidValueException e) {

                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
        });

        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);
        cancel.addClickListener(clickEvent -> window.close());

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(savePatient);
        buttons.addComponent(cancel);
        buttons.setSpacing(true);

        addPatient.addComponent(surname);
        addPatient.addComponent(firstName);
        addPatient.addComponent(patronymic);
        addPatient.addComponent(number);
        addPatient.addComponent(buttons);

        window.setContent(addPatient);
        return window;
    }

}
