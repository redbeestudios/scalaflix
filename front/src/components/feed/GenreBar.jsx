import React from 'react';
import Grid from "@material-ui/core/Grid";
import GenreButton from "./GenreButton";
import { makeStyles } from '@material-ui/core/styles';
import { LoadingSpinner } from 'video-react';

import { fetchFilms } from '../../services/FilmService';

const useStyles = makeStyles(theme => ({
    grid: {
        background: theme.palette.background.paper
    }
}));

const genreButtons = (genres, onClick) => {
    return genres
    .items
    .map((g, key) =>
    <GenreButton
        key         = { key }
        active      = { g.active }
        children    = { g.genre }
        onClick     = { onClick(g.genre) }
    />
)
};

const getFilms = (dispatch, genres) => {
    if(!genres.loading) {
        fetchFilms(dispatch, genres.items.filter(item => item.active).map(item => item.genre));
    }
};

const GenreBar = ({genres, onClick, dispatch}) => {
    const classes = useStyles();
    getFilms(dispatch, genres);
    return (
        <Grid
            container
            className={classes.grid}
            item xs={12}
            justify={"space-around"}
            alignItems={"center"}
        >
            {genres.loading ?
                LoadingSpinner :
                genreButtons(genres, onClick)
            }
        </Grid>
    );
};

export default GenreBar;