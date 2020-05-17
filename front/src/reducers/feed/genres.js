import {
    TOGGLE_GENRE,
    FETCH_GENRES_BEGIN,
    FETCH_GENRES_SUCCESS,
    FETCH_GENRES_FAILURE
} from '../../constants/feed/GenreBar'

const createTuple = (genre, active) => {
    return {
        genre: genre,
        active: active
    };
};
const initialState = {
    items: [
        createTuple("drama", false)
    ],
    loading: false,
    error: null
}

const genres = (
    state = initialState,
    action
) => {
    switch (action.type) {
        case FETCH_GENRES_BEGIN:
            return {
                ...state,
                loading: false
            }
        case FETCH_GENRES_SUCCESS:
            return {
                ...state,
                loading: false,
                items: action.payload.items.map( item =>
                    createTuple(item, true)
                )
              };
        case FETCH_GENRES_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload.error,
                items: []
            }
        case TOGGLE_GENRE:
            return {
                ...state,
                items: state.items.map(tuple =>
                    tuple.genre === action.genre ? createTuple(
                        action.genre,
                        !tuple.active
                    ) : tuple
                )
            }
        default:
            return state;
    }
};

export default genres;
