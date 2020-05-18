import React from 'react';
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Feed from './feed/Feed';
import { createMuiTheme, makeStyles } from '@material-ui/core/styles';
import AppHeader from "./AppHeader";
import Grid from "@material-ui/core/Grid";
import VideoPlayerContainer from "../containers/stream/VideoPlayerContainer";
import { fetchGenres } from '../services/GenreService';

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
        return content;
    } else {
        return <VideoPlayerContainer stream={stream}/>;
    }
};

class App extends React.Component {

  componentDidMount() {
    fetchGenres(this.props.dispatch);
  };

  render() {
    const {stream} = this.props;
    return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {streamFilmIfDefined(stream)}
      </ThemeProvider>
    );
  }
}

export default App;

