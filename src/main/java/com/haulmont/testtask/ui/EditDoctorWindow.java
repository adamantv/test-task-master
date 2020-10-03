package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.Database;
import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

//класс для создания окна "Редактирование врача"
public class EditDoctorWindow {
    //добавление нового врача
    public Window addDoctor(Grid grid) {
        final Window window = new Window("Добавить нового врача");
        window.setModal(true);
        window.center();
        window.setWidth("400");
        window.setHeight("300");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addDoctor = new FormLayout();
        addDoctor.setMargin(true);
        addDoctor.setSizeFull();

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

        final TextField specialization = new TextField("Специализация", "");
        specialization.setSizeFull();
        specialization.setRequired(true);
        specialization.setMaxLength(50);
        specialization.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final Button saveDoctor = new Button("Добавить");
        saveDoctor.setStyleName(Reindeer.BUTTON_SMALL);

        saveDoctor.addClickListener(clickEvent -> {

            try {
                surname.validate();
                firstName.validate();
                specialization.validate();

                Database.startDatabase();
                Doctor doctor = new Doctor(surname.getValue().trim(), firstName.getValue().trim(), patronymic.getValue().trim(), specialization.getValue().trim() );
                DoctorDAO.addDoctor(doctor);
                List<Doctor> doctors = DoctorDAO.getAllDoctor();

                int index = doctors.size();
                grid.addRow(doctors.get(index-1).getId(), doctors.get(index-1).getSurname(), doctors.get(index-1).getFirstName(),
                        doctors.get(index-1).getPatronymic(), doctors.get(index-1).getSpecialization());

                Database.closeDatabase();
                window.close();
            } catch (Validator.InvalidValueException e) {

                surname.setRequiredError("Введите фамилию");
                firstName.setRequiredError("Введите имя");
                specialization.setRequiredError("Введите специализацию");
                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
        });

        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);
        cancel.addClickListener(clickEvent -> window.close());

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(saveDoctor);
        buttons.addComponent(cancel);

        addDoctor.addComponent(surname);
        addDoctor.addComponent(firstName);
        addDoctor.addComponent(patronymic);
        addDoctor.addComponent(specialization);
        addDoctor.addComponent(buttons);

        window.setContent(addDoctor);
        return window;
    }

    //удаление врача
    public void deleteDoctor(Grid grid) {
        if (grid.getSelectedRow() == null) return;

        Database.startDatabase();

        Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
        Database.startDatabase();
        int result = DoctorDAO.deleteDoctor(id);
        Database.closeDatabase();

        if (result == 0) {
            grid.getContainerDataSource().removeItem(grid.getSelectedRow());
            Notification.show("Врач удален из БД");
        } else if (result == 1) {
            Notification.show("Для данного врача существует рецепт!", Notification.TYPE_ERROR_MESSAGE);
        } else Notification.show("Ошибка базы данных!",Notification.TYPE_ERROR_MESSAGE);
    }

    //внесение изменений в данные врача
    public Window updateDoctor(Grid grid) {
        final Window window = new Window("Изменить данные врача");
        window.setModal(true);
        window.center();
        window.setWidth("400");
        window.setHeight("300");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addDoctor = new FormLayout();
        addDoctor.setMargin(true);
        addDoctor.setSizeFull();

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

        final TextField specialization = new TextField("Специализация", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Специализация").getValue().toString());
        specialization.setSizeFull();
        specialization.setRequired(true);
        specialization.setRequiredError("Укажите специализацию");
        specialization.setMaxLength(50);
        specialization.addValidator(new RegexpValidator("[' 'а-яА-Яa-z-A-Z]{2,50}", true, "Введите корректные данные"));

        final Button saveDoctor = new Button("Применить");
        saveDoctor.setStyleName(Reindeer.BUTTON_SMALL);

        saveDoctor.addClickListener(clickEvent -> {

            try {
                surname.validate();
                firstName.validate();
                specialization.validate();

                Database.startDatabase();
                Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
                Doctor doctor = new Doctor(surname.getValue().trim(), firstName.getValue().trim(), patronymic.getValue().trim(), specialization.getValue().trim());
                doctor.setId(id);
                DoctorDAO.updateDoctor(doctor);
                Database.closeDatabase();

                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Фамилия").setValue(surname.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Имя").setValue(firstName.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Отчество").setValue(patronymic.getValue().trim());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Специализация").setValue(specialization.getValue().trim());
                window.close();
            } catch (Validator.InvalidValueException e) {

                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
        });

        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);
        cancel.addClickListener(clickEvent -> window.close());

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(saveDoctor);
        buttons.addComponent(cancel);
        buttons.setSpacing(true);

        addDoctor.addComponent(surname);
        addDoctor.addComponent(firstName);
        addDoctor.addComponent(patronymic);
        addDoctor.addComponent(specialization);
        addDoctor.addComponent(buttons);

        window.setContent(addDoctor);
        return window;
    }
}
