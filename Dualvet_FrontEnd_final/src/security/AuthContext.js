import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    accessToken: null,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    if (token) {
      if (isTokenExpired(token)) {
        console.log("Token is expired.");
        logout();
      } else {
        console.log("Token is valid.");
        setAuth({ isAuthenticated: true, accessToken: token });
        setLogoutTimeout(token);
      }
    }
    setLoading(false);
  }, []);

  const isTokenExpired = (token) => {
    const decodedToken = parseJwt(token);
    if (!decodedToken.exp) {
      return true;
    }
    const expirationTime = decodedToken.exp * 1000;
    return Date.now() >= expirationTime;
  };

  const parseJwt = (token) => {
    try {
      return JSON.parse(atob(token.split('.')[1]));
    } catch (error) {
      return null;
    }
  };

  const setLogoutTimeout = (token) => {
    const decodedToken = parseJwt(token);
    const expirationTime = decodedToken.exp * 1000;
    const timeLeft = expirationTime - Date.now();
    if (timeLeft > 0) {
      setTimeout(() => {
        logout();
      }, timeLeft);
    }
  };

  const login = async (user) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_ENDPOINT}/auth/authenticate`,
        user
      );
      const { access_token } = response.data;
      localStorage.setItem('access_token', access_token);
      setAuth({ isAuthenticated: true, accessToken: access_token });
      setLogoutTimeout(access_token);
    } catch (error) {
      throw new Error('Login failed');
    }
  };

  const googleLogin = async (googleToken) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_ENDPOINT}/auth/google-login`,
        { googleToken }
      );
      const { access_token } = response.data;
      localStorage.setItem('access_token', access_token);
      setAuth({ isAuthenticated: true, accessToken: access_token });
      setLogoutTimeout(access_token);
    } catch (error) {
      throw new Error('Google login failed');
    }
  };

  const logout = () => {
    localStorage.removeItem('access_token');
    setAuth({ isAuthenticated: false, accessToken: null });
  };

  return (
    <AuthContext.Provider value={{ auth, login, googleLogin, logout, loading, setLoading }}>
      {children}
    </AuthContext.Provider>
  );
};
