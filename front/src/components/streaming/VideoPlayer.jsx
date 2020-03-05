import React from 'react';
import {Player} from 'video-react';
import "../../../node_modules/video-react/dist/video-react.css";
import './VideoPlayer.css';
import Grid from "@material-ui/core/Grid";
import CloseIcon from '@material-ui/icons/Close';
import Button from "@material-ui/core/Button";

const VideoPlayer = ({stream: streamUrl, stopStream}) => {
        return (
            <Grid item container xs={12} justify={"center"} direction={"column"}>
                <Grid item container xs={12} justify={"flex-end"}>
                    <Button
                        startIcon={<CloseIcon/>}
                        onClick = { e => {
                            e.preventDefault();
                            console.log("Closing...");
                            stopStream();
                        }}
                    >
                        Close
                    </Button>
                </Grid>
                <Grid item container xs={12} justify={"center"}>
                    <div className={'video-player-backdrop'}>
                        <div className={"video-player-wrapper enter-from-top"}>
                            <Player
                                playsInline
                                autoPlay={true}
                                src={streamUrl}
                            />
                        </div>
                    </div>
                </Grid>
            </Grid>
        );
};

export default VideoPlayer;

