package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.Database;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

//класс для создания экрана со списком пациентов в табличной форме
public class PatientView {

    public VerticalLayout tabPatient() {

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setHeight("30");

        Button addPatient = new Button("Добавить");
        addPatient.addStyleName(Reindeer.BUTTON_SMALL);
        Button updatePatient = new Button("Изменить");
        updatePatient.setStyleName(Reindeer.BUTTON_SMALL);
        Button deletePatient = new Button("Удалить");
        deletePatient.setStyleName(Reindeer.BUTTON_SMALL);

        HorizontalLayout allButtons = new HorizontalLayout();
        allButtons.addComponent(addPatient);
        allButtons.addComponent(updatePatient);
        allButtons.addComponent(deletePatient);
        allButtons.setSpacing(true);

        buttons.addComponent(allButtons);
        buttons.setComponentAlignment(allButtons, Alignment.BOTTOM_LEFT);

        Grid grid = new Grid();
        grid.addColumn("ID", Long.class);
        grid.addColumn("Фамилия", String.class);
        grid.addColumn("Имя", String.class);
        grid.addColumn("Отчество", String.class);
        grid.addColumn("Номер телефона", String.class);
        grid.setWidth("65%");
        grid.setHeight("100%");
        grid.setEditorEnabled(false);
        grid.setStyleName(Reindeer.TABSHEET_SMALL);
        Grid.Column column = grid.getColumn("ID");
        column.setHidden(true);

        Database.startDatabase();
        List<Patient> patients = PatientDAO.getAllPatient();
        int index = patients.size();
        for (int i = 0; i < index; i++) {
            grid.addRow(patients.get(i).getId(), patients.get(i).getSurname(), patients.get(i).getFirstName(),
                        patients.get(i).getPatronymic(), patients.get(i).getNumber());
        }
        Database.closeDatabase();

        addPatient.addClickListener(event -> buttons.getUI().getUI().addWindow(new EditPatientWidow().addPatient(grid)));

        updatePatient.addClickListener(event -> {
            if (grid.getSelectedRow() != null)
                buttons.getUI().getUI().addWindow(new EditPatientWidow().updatePatient(grid));
        });

        deletePatient.addClickListener(event -> new EditPatientWidow().deletePatient(grid));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("100%");
        verticalLayout.addComponent(buttons);
        verticalLayout.setComponentAlignment(buttons, Alignment.TOP_LEFT);
        verticalLayout.addComponent(grid);
        verticalLayout.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeFull();

        return verticalLayout;
    }

}
