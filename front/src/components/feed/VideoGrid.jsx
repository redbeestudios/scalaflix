import Grid from '@material-ui/core/Grid';
import FeedVideo from './FeedVideo';
import React from 'react';

    import {makeStyles} from '@material-ui/core/styles';

    const useStyles = makeStyles(theme => ({
        grid: {
            wrap:"nowrap"
        }
    }));

const VideoGrid = ({ videos }) => {
    const classes = useStyles();
    return (
        <Grid className={classes.grid} container spacing={2}>
            {videos.map (feedVideo =>
                <FeedVideo
                    key={feedVideo.id}
                    {...feedVideo}
                />
            )}
        </Grid>
    );
};
export default VideoGrid;
