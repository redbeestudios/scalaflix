import { combineReducers } from 'redux';
import currentFilter from './currentFilter';
import videos from './videos';

const feed = combineReducers({
    videos: videos,
    currentFilter: currentFilter,
});

export default feed;
