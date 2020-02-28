import { LEAST_VIEWED, MOST_VIEWED } from '../../constants/feed/filterHeader';
import React from 'react';
import Grid from '@material-ui/core/Grid';
import FilterButtonContainer from '../../containers/feed/FilterButtonContainer';

import {makeStyles} from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    grid: {
      background: theme.palette.background.paper
    }
  }));

const FilterHeader = () => {
    const classes = useStyles();
    return (
        <Grid className={classes.grid} item container justify="center" xs={12}>
            <FilterButtonContainer filter={MOST_VIEWED}>Most Viewed</FilterButtonContainer>
            <FilterButtonContainer filter={LEAST_VIEWED}>Least Viewed</FilterButtonContainer>
        </Grid>
    );
};

export default FilterHeader;
