import { FETCH_VIDEOS_BEGIN, FETCH_VIDEOS_SUCCESS, FETCH_VIDEOS_FAILURE } from '../../constants/feed/videoGrid';

const initialState = {
    items: [],
    loading: false,
    error: null
}



const films = (
    state = initialState,
    action
) => {
    switch (action.type) {
        case FETCH_VIDEOS_BEGIN:
            return {
                ...state,
                loading: false
            }
        case FETCH_VIDEOS_SUCCESS:
            console.log(action.payload.items);
            return {
                ...state,
                loading: false,
                items: action.payload.items
              };
        case FETCH_VIDEOS_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload.error,
                items: []
            }
        default:
            return state;
    };
};

export default films;
