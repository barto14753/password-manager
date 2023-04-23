import React, { useState } from 'react';
import { Alert, AlertTitle } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { Content } from '../app/Content';
import AuthService from '../../services/AuthService';


export default function PasswordReset(props) {
    const [alert, setAlert] = useState({ title: '', message: '', severity: '' });

    const clearAlert = () => {
        setAlert({ title: '', message: '', severity: '' });
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        AuthService.resetPassword(
            data.get('email'), 
            data.get('old-password'), 
            data.get('new-password')
        )
        .then(() => {
            setAlert({ title: 'Successful password reset', message: "Done", severity: "success" });

        })
        .catch(err => {
            setAlert({ title: 'Error during password reset', message: err.response.data.message, severity: "error" });
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
            Password Reset
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
              name="old-password"
              label="Old password"
              type="password"
              id="old-password"
              autoComplete="current-password"
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="new-password"
              label="New password"
              type="password"
              id="new-password"
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Reset
            </Button>
          </Box>
        </Box>
      </Content>
  );
}