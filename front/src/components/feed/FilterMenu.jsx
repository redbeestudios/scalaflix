import { LEAST_VIEWED, MOST_VIEWED } from '../../constants/feed/FilterMenu';
import React from 'react';
import Grid from '@material-ui/core/Grid';

import {makeStyles} from '@material-ui/core/styles';
import MenuItem from "@material-ui/core/MenuItem";
import Button from "@material-ui/core/Button";
import Menu from "@material-ui/core/Menu";

const useStyles = makeStyles(theme => ({
    grid: {
      background: theme.palette.background.paper
    }
  }));

const FilterMenu = ( { currentFilter, setFilter }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClose = (filter) => {
        setFilter(filter);
        setAnchorEl(null);
    };

    const handleClick = event => {
        setAnchorEl(event.currentTarget);
    };

    const classes = useStyles();
    return (
        <Grid className={classes.grid} item xs={3} alignContent={'center'} justify={"center"}>
            <Button aria-controls="simple-menu" aria-haspopup="true" onClick={handleClick}>
                {currentFilter}
            </Button>
            <Menu
                id="simple-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={ e => {
                        e.preventDefault();
                        handleClose(currentFilter)
                    }
                }
            >
                <MenuItem onClick={ e => {
                        e.preventDefault();
                        handleClose(MOST_VIEWED)
                    }
                }>
                    {MOST_VIEWED}
                </MenuItem>
                <MenuItem onClick={ e => {
                        e.preventDefault();
                        handleClose(LEAST_VIEWED)
                    }
                }>
                    {LEAST_VIEWED}
                </MenuItem>
            </Menu>
        </Grid>
    );
};

export default FilterMenu;
