import React, { useState, useCallback } from "react";
import { Content } from "../app/Content";
import { connect, useDispatch } from "react-redux";
import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  TextField,
  Unstable_Grid2 as Grid,
  Alert,
  AlertTitle,
} from "@mui/material";
import ProfileService from "../../services/ProfileService";

function Profile(props) {
  const dispatch = useDispatch();
  const user = props.user;
  const [values, setValues] = useState({
    firstName: user ? user.firstName : "",
    lastName: user ? user.lastName : "",
    email: user ? user.email : "",
    phone: "",
  });

  const [alert, setAlert] = useState({ title: "", message: "", severity: "" });

  const clearAlert = () => {
    setAlert({ title: "", message: "", severity: "" });
  };

  const handleChange = useCallback((event) => {
    setValues((prevState) => ({
      ...prevState,
      [event.target.name]: event.target.value,
    }));
  }, []);

  const handleSubmit = (event) => {
    console.log("Click");
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    let newFirstName = data.get("firstName");
    let newLastName = data.get("lastName");
    ProfileService.update(dispatch, newFirstName, newLastName)
      .then(() => {
        setAlert({
          title: "Successful update",
          message: "Done",
          severity: "success",
        });
      })
      .catch((err) => {
        console.log(err);
        setAlert({
          title: "Error during update",
          message: err.response.data.message,
          severity: "error",
        });
      });
  };

  return (
    <Content>
      {alert.message && (
        <Alert onClose={() => clearAlert()} severity={alert.severity}>
          <AlertTitle>{alert.title}</AlertTitle>
          {alert.message}
        </Alert>
      )}
      <form autoComplete="off" noValidate onSubmit={handleSubmit}>
        <Card
          sx={{
            pt: 0,
            paddingTop: "2rem",
            paddingLeft: "5rem",
            paddingRight: "5rem",
          }}
        >
          <CardHeader
            subheader="The information can be edited"
            title="Profile"
          />
          <CardContent sx={{ pt: 0 }}>
            <Box sx={{ m: -1.5 }}>
              <Grid container spacing={3}>
                <Grid xs={12} md={6}>
                  <TextField
                    fullWidth
                    label="First name"
                    name="firstName"
                    onChange={handleChange}
                    required
                    value={values.firstName}
                  />
                </Grid>
                <Grid xs={12} md={6}>
                  <TextField
                    fullWidth
                    label="Last name"
                    name="lastName"
                    onChange={handleChange}
                    required
                    value={values.lastName}
                  />
                </Grid>
                <Grid xs={12} md={6}>
                  <TextField
                    fullWidth
                    label="Email Address"
                    name="email"
                    onChange={handleChange}
                    disabled
                    value={values.email}
                  />
                </Grid>
              </Grid>
            </Box>
          </CardContent>
          <CardActions sx={{ justifyContent: "flex-end" }}>
            <Button variant="contained" type="submit">
              Save details
            </Button>
          </CardActions>
        </Card>
      </form>
    </Content>
  );
}

function mapStateToProps(state) {
  const { isLoggedIn, user } = state.auth;
  return {
    isLoggedIn,
    user,
  };
}
export default connect(mapStateToProps)(Profile);
