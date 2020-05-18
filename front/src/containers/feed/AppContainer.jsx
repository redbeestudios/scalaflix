import {connect} from "react-redux";
import App from "../../components/App";
import {stopStream} from "../../actions/stream/VideoPlayer";

const mapStateToProps = (
    state
) => {
    return {
        stream: state.stream
    };
};

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        stopStream: () => {
            stopStream(ownProps.stream.id)
        },
        dispatch: dispatch
    };
};

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App);

export default AppContainer;
