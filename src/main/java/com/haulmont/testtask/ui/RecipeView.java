package com.haulmont.testtask.ui;

import com.haulmont.testtask.dao.*;
import com.haulmont.testtask.entity.*;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

//класс для создания экрана со списком рецептов в табличной форме
public class RecipeView {

    public VerticalLayout tabRecipe() {

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setHeight("30");

        Button addRecipe = new Button("Добавить");
        addRecipe.addStyleName(Reindeer.BUTTON_SMALL);
        Button updateRecipe = new Button("Изменить");
        updateRecipe.setStyleName(Reindeer.BUTTON_SMALL);
        Button deleteRecipe = new Button("Удалить");
        deleteRecipe.setStyleName(Reindeer.BUTTON_SMALL);

        HorizontalLayout allButtons = new HorizontalLayout();
        allButtons.addComponent(addRecipe);
        allButtons.addComponent(updateRecipe);
        allButtons.addComponent(deleteRecipe);
        allButtons.setSpacing(true);

        buttons.addComponent(allButtons);
        buttons.setComponentAlignment(allButtons, Alignment.BOTTOM_LEFT);

        Grid grid = new Grid();
        grid.addColumn("ID", Long.class);
        grid.addColumn("Описание", String.class).setWidth(300);
        grid.addColumn("Пациент", String.class).setWidth(180);
        grid.addColumn("Врач", String.class).setWidth(220);
        grid.addColumn("Дата создания", String.class);
        grid.addColumn("Срок действия", Integer.class);
        grid.addColumn("Приоритет", String.class);
        grid.setSizeFull();
        grid.setEditorEnabled(false);
        grid.setStyleName(Reindeer.TABLE_STRONG);
        Grid.Column column = grid.getColumn("ID");
        column.setHidden(true);

        Database.startDatabase();
        List<Recipe> recipes = RecipeDAO.getAllRecipe();
        int index = recipes.size();
        for (int i=0; i<index; i++) {
            grid.addRow(recipes.get(i).getId(), recipes.get(i).getDescription(), PatientDAO.findPatientById(recipes.get(i).getPatientID()), DoctorDAO.findDoctorById(recipes.get(i).getDoctorID()), recipes.get(i).getDataOfCreation().toString(),
                    recipes.get(i).getValidity(), recipes.get(i).getPriority());
        }
        Database.closeDatabase();

        TextArea fullDescription = new TextArea();
        fullDescription.setEnabled(false);
        fullDescription.setWidth("64.7%");
        fullDescription.setHeight("40");
        fullDescription.setStyleName(Reindeer.TEXTFIELD_SMALL);
        fullDescription.setVisible(false);

        grid.addSelectionListener((SelectionEvent.SelectionListener) event -> {

            if (grid.isSelected(grid.getSelectedRow()))
                if (grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").getValue().toString().length() > grid.getColumn("Описание").getWidth() / 10) {
                    fullDescription.setValue("Полное описание рецепта: " + grid.getContainerDataSource().getItem(grid.getSelectedRow()).getItemProperty("Описание").getValue().toString());
                    fullDescription.setVisible(true);
                    return;
                }
            fullDescription.setVisible(false);
        });

        addRecipe.addClickListener(event -> grid.getUI().getUI().addWindow(new EditRecipeWindow().addRecipe(grid, fullDescription)));

        updateRecipe.addClickListener(event -> {

            if (grid.getSelectedRow() != null)
                grid.getUI().getUI().addWindow(new EditRecipeWindow().updateRecipe(grid, fullDescription));
        });

        deleteRecipe.addClickListener(event -> new EditRecipeWindow().deleteRecipe(grid));
        FormLayout filter = new FormLayout();
        filter.addComponent(new EditRecipeWindow().filter(grid));

        HorizontalLayout horizontalLayoutTop = new HorizontalLayout();
        horizontalLayoutTop.addComponent(buttons);
        horizontalLayoutTop.setComponentAlignment(buttons, Alignment.TOP_LEFT);
        horizontalLayoutTop.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        HorizontalLayout horizontalLayoutBottom = new HorizontalLayout();
        horizontalLayoutBottom.setSpacing(true);
        horizontalLayoutBottom.setWidth(130.0f, Sizeable.Unit.PERCENTAGE);
        horizontalLayoutBottom.setHeight("100%");
        horizontalLayoutBottom.addComponent(grid);
        horizontalLayoutBottom.setComponentAlignment(grid, Alignment.TOP_LEFT);
        horizontalLayoutBottom.addComponent(filter);
        horizontalLayoutBottom.setComponentAlignment(filter, Alignment.TOP_RIGHT);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeightUndefined();
        verticalLayout.setSpacing(true);

        verticalLayout.addComponent(horizontalLayoutTop);
        verticalLayout.addComponent(horizontalLayoutBottom);
        verticalLayout.addComponent(fullDescription);

        return  verticalLayout;
    }

}

