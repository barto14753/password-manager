import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { parseJwt } from './jwtParser'
import { connect } from 'react-redux'

function AuthVerification(props) {
  const navigate = useNavigate()

  useEffect(() => {
    if (props.user && !JSON.parse(sessionStorage.getItem('user'))) {
      // props.dispatch(logout(navigate))
      logout(props.dispatch);
    } else if (props.user) {
      const decodedJwt = parseJwt(props.user.access_token)

      if (decodedJwt.exp * 1000 < Date.now()) {
        props.dispatch(logout(navigate))
      } else if (decodedJwt.exp * 1000 < Date.now() + 15 * 60 * 1000) {
      }
    }
  }, [navigate, props])

  useEffect(() => {
    const handleInvalidUser = (event) => {
      if (event.key === 'user' && event.oldValue && !event.newValue) {
        props.dispatch(logout(navigate))
      }
    }

    window.addEventListener('storage', (event) => handleInvalidUser(event))
    return () => window.removeEventListener('storage', (event) => handleInvalidUser(event))
  })

  return <div />
}

function mapStateToProps(state) {
  const { isLoggedIn, user } = state.auth
  return {
    isLoggedIn,
    user
  }
}
export default connect(mapStateToProps)(AuthVerify)
