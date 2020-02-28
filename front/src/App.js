import React from 'react';
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import feed from './reducers/feed/feed';
import Feed from './components/feed/Feed';
import { createMuiTheme, makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';

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

function App() {
  const classes = useStyles();
  return (
    <Provider store = {createStore(feed)}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Grid container justify="center" className={classes.grid}>
          <Grid item>
            <Typography gutterBottom variant="h1">Scalaflix</Typography>
          </Grid>
          <Grid item>
            <Feed />
          </Grid>
        </Grid>
      </ThemeProvider>
    </Provider>
  );
}

export default App;
