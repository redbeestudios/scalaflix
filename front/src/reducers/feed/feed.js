import { combineReducers } from 'redux';
import filter from './filter';
import videos from './videos';
import genres from './genres';

const feed = combineReducers({
    videos: videos,
    currentFilter: filter,
    genres: genres
});

export default feed;
