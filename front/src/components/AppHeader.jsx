import AppBar from '@material-ui/core/AppBar';
import Typography from "@material-ui/core/Typography";
import React from "react";
import Container from '@material-ui/core/Container';
import {Toolbar} from "@material-ui/core";

const AppHeader = () => {
    return (
        <AppBar color={"secondary"} position="static">
            <Toolbar>
                <Typography variant="h1">Scalaflix</Typography>
            </Toolbar>
        </AppBar>
    );
};

export default AppHeader;
