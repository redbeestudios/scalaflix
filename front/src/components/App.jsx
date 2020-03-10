import React from 'react';
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import feed from '../reducers/feed/feed';
import Feed from './feed/Feed';
import { createMuiTheme, makeStyles } from '@material-ui/core/styles';
import AppHeader from "./AppHeader";
import Grid from "@material-ui/core/Grid";
import VideoPlayerContainer from "../containers/stream/VideoPlayerContainer";
import {stopStream} from "../actions/stream/VideoPlayer";

const useStyles = makeStyles(theme => ({
    grid: {
      spacing: 1,
    },
    root: {
      flexGrow: 1,
    },
}));

const theme = createMuiTheme({
  palette: {
    type: 'dark',
  },
});

const content = (
        <Grid container item spacing = {2}>
            <Grid item xs={12}>
                <AppHeader/>
            </Grid>
            <Grid item xs={12}>
                <Feed />
            </Grid>
        </Grid>
    );

const streamFilmIfDefined = (stream) => {
    if(stream === "") {
        return content();
    } else {
        return <VideoPlayerContainer stream={stream}/>;
    }
};

function App({stream, stopStream}) {
  return (
      <ThemeProvider theme={theme}>
          <CssBaseline />
          {streamFilmIfDefined(stream, stopStream)}
      </ThemeProvider>
  );
}

export default App;

