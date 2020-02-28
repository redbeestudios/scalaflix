import Grid from '@material-ui/core/Grid';
import React from 'react';
import Box from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles(theme => ({
  videoLink: {
    padding: theme.spacing(2),
    textAlign: 'center',
  },
  title: {
    color: theme.palette.text.primary,
    textAlign: "left"
  },
  duration: {
    color: theme.palette.text.secondary,
    textAlign: "left",
    display: "block"
  },
  img: {
    display: 'block',
    maxWidth: '100%',
    maxHeight: '100%',
  }
}));


const FeedVideo = ({title, thumbnail, duration}) => {
  const classes = useStyles();
  return (
    <Grid className={classes.grid} item xs="3">
      <Grid pr={2} className={classes.videoLink} boxShadow={0}>
          <img className={classes.img} src={thumbnail}/>
          <Typography className={classes.title} gutterBottom variant="body2">{title}</Typography>
          <Typography className={classes.duration} gutterBottom variant="caption">{duration}</Typography>
      </Grid>      
    </Grid>
  );
}

export default FeedVideo;
