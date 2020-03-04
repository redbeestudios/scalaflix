import React from 'react';
import Grid from "@material-ui/core/Grid";
import GenreButton from "./GenreButton";
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    grid: {
        background: theme.palette.background.paper
    }
}));

const GenreBar = ({genres, onClick}) => {
    const classes = useStyles();
    return (
        <Grid className={classes.grid} item justify={"center"} sm = {9}>
            {
                genres.map((g, key) =><GenreButton
                        key         = { key }
                        active      = { g.active }
                        children    = { g.genre }
                        onClick     = { onClick(g.genre) }
                    />
                )
            }
        </Grid>
    );
};

export default GenreBar;