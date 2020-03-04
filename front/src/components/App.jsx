import React from 'react';
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import feed from '../reducers/feed/feed';
import Feed from './feed/Feed';
import { createMuiTheme, makeStyles } from '@material-ui/core/styles';
import AppHeader from "./AppHeader";
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
        <AppHeader/>
        <Feed />
      </ThemeProvider>
    </Provider>
  );
}

export default App;
