import Grid from '@material-ui/core/Grid';
import FeedVideo from './FeedVideo';
import React from 'react';

    import {makeStyles} from '@material-ui/core/styles';
import FeedVideoContainer from "../../containers/feed/FeedVideoContainer";
import { LoadingSpinner } from 'video-react';

    const useStyles = makeStyles(theme => ({
        grid: {
            wrap:"nowrap"
        }
    }));

const videoItems = (videos) => videos.loading ? LoadingSpinner : videos.items.map (feedVideo =>
        <FeedVideoContainer
            key={feedVideo.id}
            {...feedVideo}
        />
    )

const VideoGrid = ({ videos }) => {
    const classes = useStyles();
    console.log(JSON.stringify(videos));
    return (
        <Grid className={classes.grid} container spacing={2}>
            {videoItems(videos)}
        </Grid>
    );
};
export default VideoGrid;
