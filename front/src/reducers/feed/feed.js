import { combineReducers } from 'redux';
import filter from './filter';
import films from './films';
import genres from './genres';
import stream from './stream';

const feed = combineReducers({
    videos: films,
    currentFilter: filter,
    genres: genres,
    stream: stream
});

export default feed;
