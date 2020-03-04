import { MOST_VIEWED, SET_CURRENT_FILTER}  from '../../constants/feed/FilterMenu'
import getVideos from '../../services/VideoService';
const videos = (
    state = getVideos(MOST_VIEWED),
    action
) => {
    switch (action.type) {
        case SET_CURRENT_FILTER:
            return getVideos(action.filter);
        default:
            return state;
    }
};

export default videos;
