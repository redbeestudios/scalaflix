import {connect} from "react-redux";
import GenreBar from "../../components/feed/GenreBar";
import toggleGenre from "../../actions/feed/ToggleGenre";

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
        }
    };
};

const GenreBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(GenreBar);

export default GenreBarContainer;
