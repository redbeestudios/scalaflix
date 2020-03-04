import { connect } from "react-redux";
import {streamVideo} from "../../actions/stream/VideoPlayer";
import FeedVideo from "../../components/feed/FeedVideo";

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        startStream: () => {
            dispatch(streamVideo(ownProps.key))
        }
    };
};

const FeedVideoContainer = connect(null, mapDispatchToProps)(FeedVideo);

export default FeedVideoContainer;
