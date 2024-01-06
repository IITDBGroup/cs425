# Copyright (c) Jupyter Development Team.
# Distributed under the terms of the Modified BSD License.
FROM jupyter/minimal-notebook

LABEL org.opencontainers.image.authors="bglavic@uic.edu"

USER $NB_USER

# Install SQL magic and postgres library
RUN mamba install --quiet --yes \
    'jupysql' \
    'ipywidgets' \
    'psycopg2' \
	 && conda clean -ti
