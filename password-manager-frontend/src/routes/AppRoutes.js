import { Route, Routes } from 'react-router-dom'
import Register from '../components/authentication/Register'
import PageNotFound from '../components/main/PageNotFound'
import HomeRoute from './HomeRoute'
import LoginRoute from './LoginRoute'


export default function AppRoutes() {
    return (
        <Routes>
            <Route path={'/home/*'} element={<HomeRoute />} />
            <Route path={'/'} element={<HomeRoute />} />
            <Route path={'/login/*'} element={<LoginRoute />} />
            <Route path={'/register/*'} element={<Register />} />
            <Route path='*' element={<PageNotFound />} />
        </Routes>
    )
}