import { combineReducers } from 'redux';
import films from './films';
import genres from './genres';
import stream from './stream';

const feed = combineReducers({
    videos: films,
    genres: genres,
    stream: stream
});

export default feed;
