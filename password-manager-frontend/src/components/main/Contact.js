import React from 'react'
import { Content } from '../app/Content'
import MainFeaturedPost from './template/MainFeaturedPost';
import { Container, CssBaseline, Grid } from '@mui/material';
import FeaturedPost from './template/FeaturedPost';


function Contact() {

    const mainFeaturedPost = {
        title: 'Passsword Manager',
        description:
          "Here you can find useful links",
        image: 'https://source.unsplash.com/random?wallpapers',
        imageText: 'main image description',
      };

    const featuredPosts = [
        {
          title: 'Password Manager API',
          description:
            'Backend application written in Java Spring',
          image: 'https://source.unsplash.com/random?wallpapers',
          imageLabel: 'Image Text',
          link: 'https://github.com/barto14753/password-manager/tree/develop/password-manager-api'
        },
        {
          title: 'Password Manager UI',
          description:
            'Frontend application wrriten in React',
          image: 'https://source.unsplash.com/random?wallpapers',
          imageLabel: 'Image Text',
          link: 'https://github.com/barto14753/password-manager/tree/develop/password-manager-frontend'
        },
      ];
      
  return (
    <Content>
      <CssBaseline />
      <Container>
        <main>
          <MainFeaturedPost post={mainFeaturedPost} />
          <Grid container spacing={4}>
            {featuredPosts.map((post) => (
              <FeaturedPost key={post.title} post={post} />
            ))}
          </Grid>
        </main>
      </Container>
    </Content>
  )
}

export default Contact