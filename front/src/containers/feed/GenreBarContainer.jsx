import {connect} from "react-redux";
import GenreBar from "../../components/feed/GenreBar";
import { toggleGenre } from "../../actions/feed/GenreBar";

const mapStateToProps = (
    state
) => {
    return {
        genres: state.genres
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onClick: (genre) => {
            return () => dispatch(toggleGenre(genre))
        },
        dispatch: dispatch
    };
};

const GenreBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(GenreBar);

export default GenreBarContainer;
