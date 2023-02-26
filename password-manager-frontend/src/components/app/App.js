import '../app/App.css';
import Navbar from './Navbar';
import { ThemeProvider } from '@mui/material/styles';
import { theme } from '../../ThemeOptions';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from '../../routes/AppRoutes';
import Footer from './Footer';


const appStyle = {
  width: '100%',
};

function App() {
  return (
    <ThemeProvider theme={theme} style={appStyle}>
      <BrowserRouter>
        <Navbar/>
        <AppRoutes/>
        <Footer/>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
