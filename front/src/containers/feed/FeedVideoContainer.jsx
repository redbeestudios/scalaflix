import {connect} from "react-redux";
import {streamVideo} from "../../actions/stream/VideoPlayer";
import FeedVideo from "../../components/feed/FeedVideo";
import {getStream} from "../../services/FilmService";

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        startStream: () => {
            const source = getStream(ownProps.id);
            dispatch(streamVideo(source));
        }
    };
};

const FeedVideoContainer = connect(null, mapDispatchToProps)(FeedVideo);

export default FeedVideoContainer;
