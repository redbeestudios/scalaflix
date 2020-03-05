import {STREAM_VIDEO, STOP_VIDEO} from "../../constants/stream/VideoPlayer";

export const streamVideo = (source) => {
    return {
        type: STREAM_VIDEO,
        source
    };
};

export const stopStream = () => {
    return {
        type: STOP_VIDEO
    };
};
