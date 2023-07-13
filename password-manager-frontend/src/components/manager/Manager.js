import React, { useState, useCallback } from "react";
import { Content } from "../app/Content";
import { connect, useDispatch } from "react-redux";
import {
    Unstable_Grid2 as Grid,
    Alert,
    AlertTitle,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Container,
    Typography,
} from "@mui/material";
import Paper from "@mui/material/Paper";
import PasswordRow from "./PasswordRow";

function createData(id, name, created, modified, versions) {
    return { id, name, created, modified, versions };
}

const rows = [
    createData(
        0,
        "Google",
        "2015-03-25T12:00:00-06:30",
        "2015-03-25T12:00:00-06:30",
        2
    ),
    createData(
        1,
        "Facebook",
        "2015-03-25T12:00:00-06:30",
        "2015-03-25T12:00:00-06:30",
        1
    ),
    createData(
        2,
        "Twitter",
        "2015-03-25T12:00:00-06:30",
        "2015-03-25T12:00:00-06:30",
        2
    )
];

function Manager(props) {
    const dispatch = useDispatch();
    const user = props.user;

    const [alert, setAlert] = useState({
        title: "",
        message: "",
        severity: "",
    });

    const clearAlert = () => {
        setAlert({ title: "", message: "", severity: "" });
    };

    const handleChange = useCallback((event) => {
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
    };

    return (
        <Content>
            {alert.message && (
                <Alert onClose={() => clearAlert()} severity={alert.severity}>
                    <AlertTitle>{alert.title}</AlertTitle>
                    {alert.message}
                </Alert>
            )}

            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Grid container spacing={3}>
                    <Grid item xs={12}>
                        <Paper
                            sx={{
                                p: 2,
                                display: "flex",
                                flexDirection: "column",
                            }}
                        >
                            <Typography component="h1" variant="h4" color="primary" gutterBottom>
                                Passwords
                            </Typography>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell>Created</TableCell>
                                        <TableCell>Modified</TableCell>
                                        <TableCell>Versions</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {rows.map((row) => (
                                        <PasswordRow data={row} />
                                    ))}
                                </TableBody>
                            </Table>
                        </Paper>
                    </Grid>
                </Grid>
            </Container>
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
export default connect(mapStateToProps)(Manager);
