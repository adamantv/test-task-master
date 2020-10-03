package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.*;
import com.haulmont.testtask.entity.*;
import com.vaadin.data.Validator;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.validator.*;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.data.Container.*;
import java.text.SimpleDateFormat;
import java.util.*;

//класс для создания окна "Редактирование рецепта" и фильтра
public class EditRecipeWindow {
    //создание фильтра для списка рецептов
    public FormLayout filter(Grid grid){
        Label textFilter = new Label("Фильтр");
        textFilter.setStyleName(Reindeer.LABEL_H2);
        TextField patientFullName = new TextField("Пациент", "");
        patientFullName.setWidth("200");
        TextField doctorFullName = new TextField("Врач", "");
        doctorFullName.setWidth("200");
        TextField priority = new TextField("Приоритет", "");
        priority.setWidth("200");
        TextField description = new TextField("Описание", "");
        description.setWidth("200");
        description.setMaxLength(500);

        Button apply = new Button("Применить");
        apply.setStyleName(Reindeer.BUTTON_SMALL);

        apply.addClickListener(event-> {

            Filterable filterable = (Filterable) grid.getContainerDataSource();
            filterable.removeAllContainerFilters();

            Filter filterPatientFullName = new SimpleStringFilter("Пациент", patientFullName.getValue(),
                    true, false);
            Filter filterDoctorFullName = new SimpleStringFilter("Врач", doctorFullName.getValue(),
                    true, false);
            Filter filterPriority = new SimpleStringFilter("Приоритет", priority.getValue().toString(),
                    true, false);
            Filter filterDescription = new SimpleStringFilter("Описание", description.getValue().toString(),
                    true, false);

            filterable.addContainerFilter(filterPatientFullName);
            filterable.addContainerFilter(filterDoctorFullName);
            filterable.addContainerFilter(filterPriority);
            filterable.addContainerFilter(filterDescription);
        });

        FormLayout filterLayout = new FormLayout();
        filterLayout.addStyleName(Reindeer.TABLE_BORDERLESS);
        filterLayout.setWidth("500");

        filterLayout.addComponent(textFilter);
        filterLayout.addComponent(patientFullName);
        filterLayout.addComponent(doctorFullName);
        filterLayout.addComponent(priority);
        filterLayout.addComponent(description);
        filterLayout.addComponent(apply);
        return filterLayout;
    }

    //создание окна для добавления нового рецепта
    public Window addRecipe(Grid grid, TextArea fullDescription) {

        final Window window = new Window("Добавить новый рецепт");
        window.setModal(true);
        window.center();
        window.setWidth("450");
        window.setHeight("400");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addRecipe = new FormLayout();
        addRecipe.setMargin(true);
        addRecipe.setSizeFull();

        final TextField description = new TextField("Описание", "");
        description.setSizeFull();
        description.setRequired(true);
        description.setMaxLength(500);

        final DateField dataOfCreation = new DateField("Дата создания");
        Date date =  new Date();
        dataOfCreation.setValue(date);
        dataOfCreation.setSizeFull();
        dataOfCreation.setRequired(true);
        dataOfCreation.addValidator(new DateRangeValidator("Рецепт должен быть выписан не более одного месяца назад и не позднее сегодняшнего дня",
                                    new Date(date.getYear(), date.getMonth()-1, date.getDate()),
                                    new java.sql.Date(date.getTime()), Resolution.YEAR));

        final TextField validity = new TextField("Срок действия");
        validity.setRequired(true);
        validity.setSizeFull();
        validity.addValidator(new RegexpValidator("(100)|(0*\\d{1,2})", true, "Срок действия не более 100 дней"));

        List<Long> allPatientID = new ArrayList();
        List<String> allPatientFullName = new ArrayList();
        Database.startDatabase();
        List<Patient> patients = PatientDAO.getAllPatient();
        for (int i = 0; i < patients.size(); i++) {
            allPatientID.add(patients.get(i).getId());
            allPatientFullName.add(patients.get(i).toString());
        }

        //выпадающий список с данными пациентов
        ComboBox patientFullName = new ComboBox("Пациент", allPatientFullName);
        patientFullName.setSizeFull();
        patientFullName.setRequired(true);
        patientFullName.setInputPrompt("Выберите пациента");
        patientFullName.setNullSelectionAllowed(false);

        List<Long> allDoctorID = new ArrayList();
        List<String> allDoctorFullName = new ArrayList();
        Database.startDatabase();
        List<Doctor> doctors = DoctorDAO.getAllDoctor();
        for (int i = 0; i < doctors.size(); i++) {
            allDoctorID.add(doctors.get(i).getId());
            allDoctorFullName.add(doctors.get(i).toString());
        }

        //выпадающий список с данными врачей
        ComboBox doctorFullName = new ComboBox("Врач", allDoctorFullName);
        doctorFullName.setSizeFull();
        doctorFullName.setRequired(true);
        doctorFullName.setInputPrompt("Выберите врача");
        doctorFullName.setNullSelectionAllowed(false);


        List<String> allPriority = new ArrayList<>();
        allPriority.add("Нормальный");
        allPriority.add("Срочный");
        allPriority.add("Немедленный");

        //выпадающий список приоритетов
        ComboBox priority = new ComboBox("Приоритет", allPriority);
        priority.setSizeFull();
        priority.setRequired(true);
        priority.setInputPrompt("Выберите приоритет");
        priority.setNullSelectionAllowed(false);

        final Button saveRecipe = new Button("Добавить");
        saveRecipe.setStyleName(Reindeer.BUTTON_SMALL);
        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        buttons.addComponent(saveRecipe);
        buttons.addComponent(cancel);

        saveRecipe.addClickListener(clickEvent -> {

            try {
                description.validate();
                patientFullName.validate();
                doctorFullName.validate();
                dataOfCreation.validate();
                validity.validate();
                priority.validate();

                Database.startDatabase();
                Recipe recipe = new Recipe(description.getValue().trim(), allPatientID.get(allPatientFullName.indexOf(patientFullName.getValue())), allDoctorID.get(allDoctorFullName.indexOf(doctorFullName.getValue())), new java.sql.Date(dataOfCreation.getValue().getTime()),
                                        Integer.parseInt(validity.getValue().replace(',', '.')),
                                        priority.getValue().toString());
                RecipeDAO.addRecipe(recipe);
                List<Recipe> recipes = RecipeDAO.getAllRecipe();

                int size = recipes.size();

                grid.addRow(recipes.get(size-1).getId(), recipes.get(size-1).getDescription(), PatientDAO.findPatientById(recipes.get(size-1).getPatientID()), DoctorDAO.findDoctorById(recipes.get(size-1).getDoctorID()), recipes.get(size-1).getDataOfCreation().toString(),
                        recipes.get(size-1).getValidity(), recipes.get(size-1).getPriority());
                Database.closeDatabase();

                if (description.getValue().length() > grid.getColumn("Описание").getWidth() / 10) {
                    fullDescription.setValue("Полное описание рецепта: " + grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").getValue().toString());
                    fullDescription.setVisible(true);
                } else fullDescription.setVisible(false);
                window.close();

            } catch (Validator.InvalidValueException e) {

                description.setRequiredError("Введите описание рецепта");
                patientFullName.setRequiredError("Укажите пациента");
                doctorFullName.setRequiredError("Укажите врача");
                validity.setRequiredError("Введите срок действия рецепта");
                priority.setRequiredError("Укажите приоритет рецепта");
                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
        });

        cancel.addClickListener(clickEvent -> window.close());

        addRecipe.addComponent(description);
        addRecipe.addComponent(patientFullName);
        addRecipe.addComponent(doctorFullName);
        addRecipe.addComponent(dataOfCreation);
        addRecipe.addComponent(validity);
        addRecipe.addComponent(priority);
        addRecipe.addComponent(buttons);

        window.setContent(addRecipe);
        return window;
    }

    //удаление рецепта
    public  void deleteRecipe(Grid grid){

        if (grid.getSelectedRow() == null) return;

        Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
        Database.startDatabase();
        RecipeDAO.deleteRecipe(id);
        Database.closeDatabase();

        grid.getContainerDataSource().removeItem(grid.getSelectedRow());
        Notification.show("Рецепт удален из БД");
    }

    //создание окна для внесения изменений в данные рецепта
    public Window updateRecipe(Grid grid, TextArea fullDescription) {

        final Window window = new Window("Изменение рецепта");
        window.setModal(true);
        window.center();
        window.setWidth("450");
        window.setHeight("400");
        window.setClosable(false);
        window.setResizable(false);

        final FormLayout addRecipe = new FormLayout();
        addRecipe.setMargin(true);
        addRecipe.setSizeFull();

        final TextField description = new TextField("Описание", grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").getValue().toString());
        description.setSizeFull();
        description.setRequired(true);
        description.setRequiredError("Опишите рецепт");
        description.setMaxLength(500);

        SimpleDateFormat dateFotmat = new SimpleDateFormat("yyyy-MM-dd");

        final DateField dataOfCreation = new DateField("Дата создания");
        Date date =  new Date();
        try {
            dataOfCreation.setValue(dateFotmat.parse(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Дата создания").getValue().toString()));
        } catch(Exception e) {
            System.out.println("Ошибка в считывании даты создания рецпета");
        }
        dataOfCreation.setSizeFull();
        dataOfCreation.setRequired(true);

        dataOfCreation.addValueChangeListener(event -> {
                    try {
                        dataOfCreation.setRequiredError("Необходимо указать дату создания рецепта");
                        dataOfCreation.removeAllValidators();
                        dataOfCreation.addValidator(new DateRangeValidator("Рецепт должен быть выписан не более одного месяца назад и не позднее сегодняшнего дня",
                                new Date(date.getYear(), date.getMonth() - 1, date.getDate()),
                                new java.sql.Date(date.getTime()), Resolution.YEAR));
                    } catch (NullPointerException e) {
                Notification.show("Ошибка введенных дат");
            }
        } );

        final TextField validity = new TextField("Срок действия",  grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Срок действия").getValue().toString());
        validity.setSizeFull();
        validity.setRequired(true);
        validity.setRequiredError("Укажите срок действия");

        validity.addValidator(new RegexpValidator("[1-9]{1}$|^[1-9]{1}[0-9]{1}$|^100 ", true, "Срок действия не может быть более 99 дней или отрицательным"));

        List<Long> allPatientID = new ArrayList();
        List<String> allPatientFullName = new ArrayList();
        Database.startDatabase();
        List<Patient> patients = PatientDAO.getAllPatient();
        for (int i = 0; i < patients.size(); i++) {
            allPatientID.add(patients.get(i).getId());
            allPatientFullName.add(patients.get(i).toString());
        }

        //выпадающий список с данными пациентов
        ComboBox patientFullName = new ComboBox("Пациент", allPatientFullName);
        patientFullName.setSizeFull();
        patientFullName.setRequired(false);
        patientFullName.setValue(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Пациент").getValue().toString());
        //patientFullName.setValue(Long.parseLong(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID пациента").getValue().toString()));
        patientFullName.setNullSelectionAllowed(false);


        List<Long> allDoctorID = new ArrayList();
        List<String> allDoctorFullName = new ArrayList();
        Database.startDatabase();
        List<Doctor> doctors = DoctorDAO.getAllDoctor();
        for (int i = 0; i < doctors.size(); i++) {
            allDoctorID.add(doctors.get(i).getId());
            allDoctorFullName.add(doctors.get(i).toString());
        }

        //выпадающий список с данными врачей
        ComboBox doctorFullName = new ComboBox("Врач", allDoctorFullName);
        doctorFullName.setSizeFull();
        doctorFullName.setRequired(false);
        doctorFullName.setValue(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Врач").getValue().toString());
        doctorFullName.setNullSelectionAllowed(false);


        List<String> allPriority = new ArrayList<>();
        allPriority.add("Нормальный");
        allPriority.add("Срочный");
        allPriority.add("Немедленный");

        //выпадающий список приоритетов
        ComboBox priority = new ComboBox("Приоритет", allPriority);
        priority.setSizeFull();
        priority.setValue(grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Приоритет").getValue().toString());
        priority.setNullSelectionAllowed(false);
        priority.setRequired(false);

        final Button updateRecipe = new Button("Изменить");
        updateRecipe.setStyleName(Reindeer.BUTTON_SMALL);
        final Button cancel = new Button("Отмена");
        cancel.setStyleName(Reindeer.BUTTON_SMALL);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(updateRecipe);
        buttons.addComponent(cancel);
        buttons.setSpacing(true);

        updateRecipe.addClickListener(clickEvent -> {
            try {
                description.validate();
                patientFullName.validate();
                doctorFullName.validate();
                dataOfCreation.validate();
                //dataOfCompletion.validate();
                validity.validate();
                priority.validate();

                Database.startDatabase();
                Long id = (Long) grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("ID").getValue();
                Recipe recipe = new Recipe(description.getValue().trim(), allPatientID.get(allPatientFullName.indexOf(patientFullName.getValue())), allDoctorID.get(allDoctorFullName.indexOf(doctorFullName.getValue())), new java.sql.Date(dataOfCreation.getValue().getTime()), Integer.parseInt(validity.getValue().replace(',', '.')), priority.getValue().toString());
                recipe.setId(id);
                RecipeDAO.updateRecipe(recipe);
                Database.closeDatabase();

                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").setValue(description.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Пациент").setValue(patientFullName.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Врач").setValue(doctorFullName.getValue());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Дата создания").setValue(new java.sql.Date(dataOfCreation.getValue().getTime()).toString());
                //grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Дата окончания работ").setValue(new java.sql.Date(dataOfCompletion.getValue().getTime()).toString());
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Срок действия").setValue(Integer.parseInt(validity.getValue().replace(',', '.')));
                grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Приоритет").setValue(priority.getValue());

                if (description.getValue().length() > grid.getColumn("Описание").getWidth() / 10) {
                    fullDescription.setValue("Полное описание рецепта: " + grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").getValue().toString());
                    fullDescription.setVisible(true);
                } else fullDescription.setVisible(false);

                window.close();
            } catch (Validator.InvalidValueException e) {

                Notification.show("Заполните данные", Notification.TYPE_WARNING_MESSAGE);
            }
         });

        cancel.addClickListener(clickEvent -> {
            window.close();
        });

        addRecipe.addComponent(description);
        addRecipe.addComponent(patientFullName);
        addRecipe.addComponent(doctorFullName);
        addRecipe.addComponent(dataOfCreation);
        addRecipe.addComponent(validity);
        addRecipe.addComponent(priority);
        addRecipe.addComponent(buttons);

        window.setContent(addRecipe);
        return window;
    }

}
