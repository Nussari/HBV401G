module g1t.hbv401g {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens g1t.hbv401g.controller to javafx.fxml;
    opens g1t.hbv401g.view to javafx.fxml;
    

    exports g1t.hbv401g.model;
    exports g1t.hbv401g.controller;
    exports g1t.hbv401g.view;

    exports g1t.teamD.model;
    exports g1t.teamD.controller;

    exports g1t.teamF.controller;
    exports g1t.teamF.model;
    exports g1t.teamF.db;
}