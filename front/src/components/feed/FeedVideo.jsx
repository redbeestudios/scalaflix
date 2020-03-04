import Grid from '@material-ui/core/Grid';
import React from 'react';
import Box from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import HtmlTooltip from "./HtmlTooltip";
import Button from "@material-ui/core/Button";

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
  },
  button: {
    display: 'block'
  }
}));


const FeedVideo = ({title, thumbnail, duration, description, startStream}) => {
  const classes = useStyles();
  return (
    <Grid className={classes.grid} item xs={3}>
        <HtmlTooltip title={
            <React.Fragment>
                {description}
            </React.Fragment>}
        >
      <Grid pr={2} className={classes.videoLink}>
          <Button className={classes.button} onClick= { e => {
              e.preventDefault();
              startStream()
          }}>
              <img className={classes.img} src={thumbnail}/>
          </Button>
          <Typography className={classes.title} gutterBottom variant="body2">{title}</Typography>
          <Typography className={classes.duration} gutterBottom variant="caption">{duration}</Typography>
      </Grid>
        </HtmlTooltip>
    </Grid>
  );
}

export default FeedVideo;
