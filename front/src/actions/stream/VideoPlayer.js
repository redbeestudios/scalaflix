import {STREAM_VIDEO, STOP_VIDEO} from "../../constants/stream/VideoPlayer";

export const streamVideo = (id) => {
    return {
        type: STREAM_VIDEO,
        id
    };
};

export const stopStream = () => {
    return {
        type: STOP_VIDEO
    };
};
