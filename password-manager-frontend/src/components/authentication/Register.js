import React, { useState } from 'react';
import { Alert, AlertTitle } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { Content } from '../app/Content';
import { RouteNames } from '../../routes/RouteNames';
import AuthService from '../../services/AuthService';


export default function Register() {

  const [alert, setAlert] = useState({ title: '', message: '', severity: '' });

  const clearAlert = () => {
    setAlert({ title: '', message: '', severity: '' });
  }

  const handleSubmit = async (event) => {
    const form = event.target.form;
    AuthService.register(
      form[4].value,
      form[0].value,
      form[2].value,
      form[6].value
    )
    .then(() => {
      setAlert({ title: 'Successful registration', message: "Done", severity: "success" });
    })
    .catch(err => {
      setAlert({ title: 'Error during register', message: err.response.data.message, severity: "error" });
    })
  };

  return (
    <Content>
      {alert.message && (
            <Alert onClose={() => clearAlert()} severity={alert.severity}>
                <AlertTitle>{alert.title}</AlertTitle>
                {alert.message}
            </Alert>
        )}
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box id="registerForm" component="form" sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                />
              </Grid>
            </Grid>
            <Button
              onClick={handleSubmit}
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href={RouteNames.LOGIN} variant="body2">
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </Content>
  );
}