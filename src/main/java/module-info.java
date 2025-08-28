module it.polimi.is25ll.glaxy_trucker_project {
    // Dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.jetbrains.annotations;
    requires org.json;
    requires java.desktop;

    opens it.polimi.ingsoftware.ll13.client.view.gui to javafx.fxml;
    opens it.polimi.ingsoftware.ll13 to javafx.fxml;

    //
     // GAME MODEL
    //
    // Main package
    exports it.polimi.ingsoftware.ll13.model;
    // Sub packages
    exports it.polimi.ingsoftware.ll13.model.cards;
    // Sub sub packages
    exports it.polimi.ingsoftware.ll13.model.cards.cards_objects;
    exports it.polimi.ingsoftware.ll13.model.cards.decks;
    exports it.polimi.ingsoftware.ll13.model.cards.dtos;

    exports it.polimi.ingsoftware.ll13.model.crew_members;
    exports it.polimi.ingsoftware.ll13.model.hourglass;
    exports it.polimi.ingsoftware.ll13.model.json_parser;
    exports it.polimi.ingsoftware.ll13.model.map;
    exports it.polimi.ingsoftware.ll13.model.player;
    exports it.polimi.ingsoftware.ll13.model.ship_board;
    exports it.polimi.ingsoftware.ll13.model.ship_board.helpers;
    exports it.polimi.ingsoftware.ll13.model.ship_board.ship_components;
    exports it.polimi.ingsoftware.ll13.model.ship_board.ship_creator;

    //
     // UTILS
    //
    exports it.polimi.ingsoftware.ll13.utils;
    exports it.polimi.ingsoftware.ll13.utils.input;
    exports it.polimi.ingsoftware.ll13.utils.json;

    //
     // SERVER
    //
    exports it.polimi.ingsoftware.ll13.server;
    exports it.polimi.ingsoftware.ll13.server.controller;

    //
     // CLIENT
    //
    exports it.polimi.ingsoftware.ll13.client;
    exports it.polimi.ingsoftware.ll13.client.controller;
    exports it.polimi.ingsoftware.ll13.client.exceptions;
    exports it.polimi.ingsoftware.ll13.client.handlers;
    exports it.polimi.ingsoftware.ll13.client.utils.ansi;
    exports it.polimi.ingsoftware.ll13.client.utils.printers;
    exports it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers;
    exports it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;
    exports it.polimi.ingsoftware.ll13.client.view.gui;
    exports it.polimi.ingsoftware.ll13.client.view.cli;

    //
     // NETWORK
    //
    exports it.polimi.ingsoftware.ll13.network.connection;
    exports it.polimi.ingsoftware.ll13.network.requests;
    exports it.polimi.ingsoftware.ll13.network.response;

    // If you have a main application class that starts the JavaFX app:
    exports it.polimi.ingsoftware.ll13;

}