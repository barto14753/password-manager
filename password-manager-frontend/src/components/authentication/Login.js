import React, { useState } from 'react';
import { Alert, AlertTitle } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { Content } from '../app/Content';
import { RouteNames } from '../../routes/RouteNames';
import AuthService from '../../services/AuthService';
import { useNavigate } from 'react-router-dom';



export default function Login(props) {
  const navigate = useNavigate()

  const [alert, setAlert] = useState({ title: '', message: '', severity: '' });
  const [redirect, setRedirect] = useState(false);

  const clearAlert = () => {
    setAlert({ title: '', message: '', severity: '' });
}

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    AuthService.login(
      data.get('email'), 
      data.get('password')
    )
    .then(() => {
      setAlert({ title: 'Successful login', message: "Done", severity: "success" });
      setRedirect(true);
    })
    .catch(err => {
      console.log(err);
      setAlert({ title: 'Error during login', message: err.response.data.message, severity: "error" });
    })

    if (redirect) {
      navigate(RouteNames.PROFILE);
    }
  };

  return (
    <Content>
      {alert.message && (
            <Alert onClose={() => clearAlert()} severity={alert.severity}>
                <AlertTitle>{alert.title}</AlertTitle>
                {alert.message}
            </Alert>
        )}
    <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href={RouteNames.PASSOWRD_RESET} variant="body2">
                  Want to change a password?
                </Link>
              </Grid>
              <Grid item>
                <Link href={RouteNames.REGISTER} variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Content>
  );
}