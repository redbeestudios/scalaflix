import React from 'react';
import VideoGrid from '../../components/feed/VideoGrid';
import { connect } from 'react-redux';

const mapStateToProps = (state) => {
    return {
        videos: state.videos
    };
};

const VideoGridContainer = connect(mapStateToProps, null)(VideoGrid);

export default VideoGridContainer;
