import { connect } from 'react-redux';
import VideoPlayer from "../../components/streaming/VideoPlayer";
import {stopStream} from "../../actions/stream/VideoPlayer";

const mapDispatchToProps = (dispatch) => {
    return {
        stopStream: () => {
            console.log("stopping video");
            dispatch(stopStream());
        }
    };
};

const VideoPlayerContainer = connect(null, mapDispatchToProps)(VideoPlayer);

export default VideoPlayerContainer