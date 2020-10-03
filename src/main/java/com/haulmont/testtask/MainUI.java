package com.haulmont.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import com.haulmont.testtask.ui.*;

@Theme(Reindeer.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        Label workshop = new Label("");
        workshop.setStyleName(Reindeer.LABEL_H1);
        workshop.setHeight("1");
        workshop.setWidth(10, Unit.PICAS);

        Label title = new Label("");
        title.setStyleName(Reindeer.LABEL_H2);
        workshop.setHeight("20");
        workshop.setWidth(10, Unit.PICAS);

        HorizontalLayout text = new HorizontalLayout();
        text.setWidth(98, Unit.PICAS);
        text.addComponent(workshop);
        text.setComponentAlignment(workshop, Alignment.MIDDLE_CENTER);


        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidth(98.0f, Unit.PICAS);
        tabSheet.setHeight(98.0f, Unit.PERCENTAGE);
        tabSheet.addTab(new PatientView().tabPatient(), "   Пациенты   ");
        tabSheet.addTab(new DoctorView().tabDoctor(), "   Врачи   ");
        tabSheet.addTab(new RecipeView().tabRecipe(), "   Рецепты   ");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(tabSheet);
        mainLayout.setComponentAlignment(tabSheet, Alignment.TOP_CENTER);
        setContent(mainLayout);

    }
}