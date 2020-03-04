import { MOST_VIEWED, SET_CURRENT_FILTER}  from '../../constants/feed/FilterMenu'
import { getFilms } from '../../services/FilmService';
const films = (
    state = getFilms(MOST_VIEWED),
    action
) => {
    switch (action.type) {
        case SET_CURRENT_FILTER:
            return getFilms(action.filter);
        default:
            return state;
    }
};

export default films;
