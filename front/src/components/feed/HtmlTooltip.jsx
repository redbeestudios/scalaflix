import { withStyles } from '@material-ui/core/styles';
import Tooltip from '@material-ui/core/Tooltip';


const HtmlTooltip = withStyles(theme => ({
    tooltip: {
        backgroundColor: theme.palette.common.black,
        color: theme.palette.common.white,
        maxWidth: 220,
        fontSize: theme.typography.pxToRem(12),
        border: '1px solid #dadde9',
    },
}))(Tooltip);

export default HtmlTooltip;
