import React, { useState, useCallback } from 'react';
import { Content } from '../app/Content';
import { connect } from 'react-redux'
  import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    Divider,
    TextField,
    Unstable_Grid2 as Grid
  } from '@mui/material';
  
  
  function Profile(props) {
    const user = props.user
    const [values, setValues] = useState({
      firstName: user ? user.firstName : '',
      lastName: user ? user.lastName : '',
      email: user ? user.email : '',
      phone: ''
    });
  
    const handleChange = useCallback(
      (event) => {
        setValues((prevState) => ({
          ...prevState,
          [event.target.name]: event.target.value
        }));
      },
      []
    );
  
    const handleSubmit = useCallback(
      (event) => {
        event.preventDefault();
      },
      []
    );
  
    return (
    <Content>
      <form
        autoComplete="off"
        noValidate
        onSubmit={handleSubmit}
      >
        <Card>
          <CardHeader
            subheader="The information can be edited"
            title="Profile"
          />
          <CardContent sx={{ pt: 0 }}>
            <Box sx={{ m: -1.5 }}>
              <Grid
                container
                spacing={3}
              >
                <Grid
                  xs={12}
                  md={6}
                >
                  <TextField
                    fullWidth
                    label="First name"
                    name="firstName"
                    onChange={handleChange}
                    required
                    value={values.firstName}
                  />
                </Grid>
                <Grid
                  xs={12}
                  md={6}
                >
                  <TextField
                    fullWidth
                    label="Last name"
                    name="lastName"
                    onChange={handleChange}
                    required
                    value={values.lastName}
                  />
                </Grid>
                <Grid
                  xs={12}
                  md={6}
                >
                  <TextField
                    fullWidth
                    label="Email Address"
                    name="email"
                    onChange={handleChange}
                    required
                    value={values.email}
                  />
                </Grid>
              </Grid>
            </Box>
          </CardContent>
          <Divider />
          <CardActions sx={{ justifyContent: 'flex-end' }}>
            <Button variant="contained">
              Save details
            </Button>
          </CardActions>
        </Card>
      </form>
    </Content>

    );
  };

  function mapStateToProps(state) {
    const { isLoggedIn, user } = state.auth
    return {
      isLoggedIn,
      user
    }
  }
  export default connect(mapStateToProps)(Profile)