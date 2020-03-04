import {getStream} from "../../services/FilmService";
import {STOP_VIDEO, STREAM_VIDEO} from "../../constants/stream/VideoPlayer";

const stream = (state = "", action) => {
    switch(action.type) {
        case STREAM_VIDEO:
            return getStream(action.id);
        case STOP_VIDEO:
            return "";
        default:
            return state;
    }
};

export default stream;