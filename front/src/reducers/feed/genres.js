import { TOGGLE_GENRE } from '../../constants/feed/FilterMenu'
import { getGenres } from '../../services/GenreService'

const createTuple = (genre, active) => {
    return {
        genre: genre,
        active: active
    };
};


const genres = (
    state = getGenres().map(genre => (createTuple(genre, true))),
    action
) => {
    switch (action.type) {
        case TOGGLE_GENRE:
            return state.map(tuple => tuple.genre === action.genre ? createTuple(action.genre, !tuple.active) : tuple);
        default:
            return state;
    }
};

export default genres;
