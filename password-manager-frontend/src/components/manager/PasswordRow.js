import React from "react";
import { useDispatch } from "react-redux";
import {
    TableRow,
    TableCell,
} from "@mui/material";


function PasswordRow(props) {
    const data = props.data
    const dispatch = useDispatch();

    return (
        <TableRow key={data.id}>
            <TableCell>{data.name}</TableCell>
            <TableCell>{data.created}</TableCell>
            <TableCell>{data.modified}</TableCell>
            <TableCell>{data.versions}</TableCell>
        </TableRow>
    );
}

export default PasswordRow