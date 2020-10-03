package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.*;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

//класс для создания экрана со списком врачей в табличной форме
public class DoctorView {

    public VerticalLayout tabDoctor() {

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setHeight("30");

        Button addDoctor = new Button("Добавить");
        addDoctor.addStyleName(Reindeer.BUTTON_SMALL);
        Button updateDoctor = new Button("Изменить");
        updateDoctor.setStyleName(Reindeer.BUTTON_SMALL);
        Button deleteDoctor = new Button("Удалить");
        deleteDoctor.setStyleName(Reindeer.BUTTON_SMALL);

        HorizontalLayout allButtons = new HorizontalLayout();
        allButtons.addComponent(addDoctor);
        allButtons.addComponent(updateDoctor);
        allButtons.addComponent(deleteDoctor);
        allButtons.setSpacing(true);

        buttons.addComponent(allButtons);
        buttons.setComponentAlignment(allButtons, Alignment.BOTTOM_LEFT);

        Grid grid = new Grid();
        grid.addColumn("ID", Long.class);
        grid.addColumn("Фамилия", String.class);
        grid.addColumn("Имя", String.class);
        grid.addColumn("Отчество", String.class);
        grid.addColumn("Специализация", String.class);
        grid.setWidth("65%");
        grid.setHeight("100%");
        grid.setEditorEnabled(false);
        grid.setStyleName(Reindeer.TABSHEET_SMALL);
        Grid.Column column = grid.getColumn("ID");
        column.setHidden(true);

        Database.startDatabase();

        List<Doctor> doctors = DoctorDAO.getAllDoctor();
        for (int i = 0; i < doctors.size(); i++) {
            grid.addRow(doctors.get(i).getId(), doctors.get(i).getSurname(), doctors.get(i).getFirstName(),
                        doctors.get(i).getPatronymic(), doctors.get(i).getSpecialization());
        }
        Database.closeDatabase();

        addDoctor.addClickListener(event -> buttons.getUI().getUI().addWindow(new EditDoctorWindow().addDoctor(grid)));

        updateDoctor.addClickListener(event -> {
            if (grid.getSelectedRow() != null)
                buttons.getUI().getUI().addWindow(new EditDoctorWindow().updateDoctor(grid));
        });

        deleteDoctor.addClickListener(event -> new EditDoctorWindow().deleteDoctor(grid));

        VerticalLayout verticalLayoutDoctor = new VerticalLayout();
        verticalLayoutDoctor.setHeight("100%");
        verticalLayoutDoctor.addComponent(buttons);
        verticalLayoutDoctor.setComponentAlignment(buttons, Alignment.TOP_LEFT);
        verticalLayoutDoctor.addComponent(grid);
        verticalLayoutDoctor.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        verticalLayoutDoctor.setSpacing(true);
        verticalLayoutDoctor.setSizeFull();

        return verticalLayoutDoctor;
    }
}
