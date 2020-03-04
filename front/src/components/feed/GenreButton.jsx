import Button from '@material-ui/core/Button';
import React from 'react';
import {makeStyles} from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
  button: {
  }
}));

const GenreButton = ({
    active,
    children,
    onClick
  }) => { 
      const classes = useStyles();
      return (
        <Button className={classes.button}
            variant={active ? "contained" : "outlined"}
            color="secondary"
            onClick={ e => {
                e.preventDefault();
                onClick();
              }}
        >
            {children}
        </Button>
    )
};

export default GenreButton;
